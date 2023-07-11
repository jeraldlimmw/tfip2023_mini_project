import { Component, inject } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { Bill } from '../models';
import { BillService } from '../bill.service';

@Component({
  selector: 'app-percent-bill',
  templateUrl: './percent-bill.component.html',
  styleUrls: ['./percent-bill.component.css']
})
export class PercentBillComponent {

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
    this.currentBill.byPercentage = true
    this.form = this.createForm()
  }

  // Create form
  createForm() {
    this.itemArr = this.fb.array([])
    this.addItem('item')
    return this.fb.group({
      items: this.itemArr,
      service: this.fb.control<number>(10, [ Validators.required, Validators.min(0) ]),
      tax: this.fb.control<number>(8, [ Validators.required, Validators.min(0) ])
    })
  }

  // Buttons
  // add and remove item from ItemArr
  addItem(s: string | null) {
    this.itemForm = this.fb.group({
      itemName: this.fb.control<string>(!!s? s : '', 
          [ Validators.required, Validators.minLength(2) ]),
      price: this.fb.control<number>(0, [ Validators.required, Validators.min(0.01) ]),
      quantity: this.fb.control<number>(1, [ Validators.required, Validators.min(1) ])
    })
    this.addPercentShare(this.currentBill.friends.length)
    this.itemArr.push(this.itemForm)
  }
  
  addPercentShare(max: number) {
    for(let i = 0; i < max; i++) {
      this.itemForm.addControl(`percent${i}`, this.fb.control<number>(0, [ Validators.required, Validators.min(0) ]))
    }
  }

  removeItem(i: number) {
    this.itemArr.removeAt(i)
  }

  exclude(s: string) {
    this.form.get(s)?.patchValue(0)
  }

  splitByShare() {
    this.router.navigate(['/bill-share'])
  }

  // Post bill to backend
  createPercentArr(c: any): number[] {
    const percentArr: number[] = []
    for (let i = 0; i < this.currentBill.friends.length; i++) {
      percentArr.push(c.get(`percent${i}`).value / 100)
    }
    return percentArr
  }

  saveBill() {
    this.currentBill.service = this.form.value.service
    this.currentBill.tax = this.form.value.tax
    this.currentBill.items = this.itemArr.controls
        .map(control => ({
          itemName: control.value.itemName,
          price: control.value.price,
          quantity: control.value.quantity,
          shares: [],
          percentShares: this.createPercentArr(control)
      }))
    console.info(this.currentBill)
    
    firstValueFrom(this.billSvc.postBillSplit(this.currentBill))
      .then(result => {
        console.info(result)
      })
      .catch(err => {
        alert(JSON.stringify(err))
      })
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
  
  not100Percent(i: number) {
    let totalPercentage = 0
    for (let j = 0; j < this.currentBill.friends.length; j++) {
      totalPercentage += this.itemArr.at(i).get(`percent${j}`)?.value
    }
    return totalPercentage != 100 && this.itemArr.at(i).dirty
  }

  incorrectTotal() {
    return this.totalPrice < (this.currentBill.total - 0.01) || 
        this.totalPrice > (this.currentBill.total + 0.01)
  }

  invalidForm() {
    let rowsWithout100Percent = 0
    for (let i = 0; i < this.itemArr.length; i++) {
      if (this.not100Percent(i)) rowsWithout100Percent++
    }
    return this.form.invalid || this.incorrectTotal() || rowsWithout100Percent > 0
  }
}
