import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { BillService } from '../bill.service';
import { Bill } from '../models';

@Component({
  selector: 'app-add-items',
  templateUrl: './add-items.component.html',
  styleUrls: ['./add-items.component.scss']
})
export class AddItemsComponent {

  links = ['Step 1: Add Friends & Expenditure', 'Step 2: Add Items', 'Step 3: Split Bill']
  activeLink = this.links[1];

  fb = inject(FormBuilder)
  router = inject(Router)
  billSvc = inject(BillService)

  form!: FormGroup
  itemArr!: FormArray
  itemForm!: FormGroup
  currentBill!: Bill
  totalPrice = 0

  ngOnInit(): void {
    this.currentBill = this.billSvc.bill
    this.form = this.createForm()
    this.calculateTotalPrice()
  }

  // Create forms and arrays
  createForm() {
    this.itemArr = this.fb.array([])
    if (this.currentBill.items.length < 1) {
      this.addItem('item', 0)
    } else {
      this.currentBill.items.forEach(item => {
        this.addItem(item.itemName, item.price)
      })
    }
    return this.fb.group({
      items: this.itemArr,
      service: this.fb.control<number>(0, [ Validators.required, Validators.min(0) ]),
      tax: this.fb.control<number>(0, [ Validators.required, Validators.min(0) ])
    })
  }

  // Buttons
  // Add and remove item from ItemArr
  addItem(s: string | null, p: number | null) {
    this.itemForm = this.fb.group({
      itemName: this.fb.control<string>(!!s? s : '', 
          [ Validators.required, Validators.minLength(2) ]),
      price: this.fb.control<number>(!!p? p : 0, 
          [ Validators.required, Validators.min(0.01) ]),
      quantity: this.fb.control<number>(1, [ Validators.required, Validators.min(1) ])
    })
    this.itemArr.push(this.itemForm)
  }

  removeItem(i: number) {
    this.itemArr.removeAt(i)
  }

  addExtra(s: string) {
    if (s === 'tax') this.form.get(s)?.patchValue(8)
    if (s === 'service') this.form.get(s)?.patchValue(10)
  }

  saveItems() {
    this.currentBill.service = this.form.value.service
    this.currentBill.tax = this.form.value.tax
    this.currentBill.items = this.itemArr.controls
        .map(control => ({
          itemName: control.value.itemName,
          price: control.value.price,
          quantity: control.value.quantity,
          shares: [],
          percentShares: []
      }))
    console.info(this.currentBill)
    this.billSvc.bill = this.currentBill
    if (this.currentBill.byPercentage) {
      this.router.navigate(['/bill-percent'])
    } else {
      this.router.navigate(['/bill-share'])
    }
    
  }

  toFriends() {
    this.currentBill.service = this.form.value.service
    this.currentBill.tax = this.form.value.tax
    this.currentBill.items = this.itemArr.controls
        .map(control => ({
          itemName: control.value.itemName,
          price: control.value.price,
          quantity: control.value.quantity,
          shares: [],
          percentShares: []
      }))
    console.info(this.currentBill)
    this.billSvc.bill = this.currentBill
    this.router.navigate(['/'])
  }

  // Validations
  calculateTotalPrice() {
    this.totalPrice = 0
    for (let i = 0; i < this.itemArr.length; i++) {
      const quantity = this.itemArr.at(i).get('quantity')?.value
      const price = this.itemArr.at(i).get('price')?.value
      this.totalPrice += (quantity * price)
    }
    this.totalPrice = this.totalPrice * (1 + this.form.get('service')?.value/100) 
                        * (1 + this.form.get('tax')?.value/100)
  }

  oneItemLeft() {
    return this.itemArr.length < 2
  }

  noName(i: number, itemName: string) {
    return !!(this.itemArr.at(i).get(itemName)?.invalid 
        && this.itemArr.at(i).get(itemName)?.dirty)
  }

  lessThanZero(i: number, price: string, quantity: string) {
    return !!(this.itemArr.at(i).get(price)?.invalid 
        && this.itemArr.at(i).get(price)?.dirty) || 
        !!(this.itemArr.at(i).get(quantity)?.invalid 
        && this.itemArr.at(i).get(quantity)?.dirty)
  }

  incorrectTotal() {
    return this.totalPrice < (this.currentBill.total - 0.025) || 
        this.totalPrice > (this.currentBill.total + 0.025)
  }

  invalidForm() {
    return this.form.invalid || this.incorrectTotal()
  }

  // Go to receipt-snap component:
  addReceipt() {
    this.billSvc.bill = this.currentBill
    this.router.navigate(['/receipt'])
  }
}
