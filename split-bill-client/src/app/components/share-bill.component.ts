import { Component, OnInit, inject } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BillService } from '../bill.service';
import { Router } from '@angular/router';
import { Bill, Item } from '../models';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-bill',
  templateUrl: './share-bill.component.html',
  styleUrls: ['./share-bill.component.css']
})
export class ShareBillComponent implements OnInit{

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
    this.currentBill.byPercentage = false
    this.form = this.createForm()
  }

  // Create forms and arrays
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
  // Add and remove item from ItemArr
  addItem(s: string | null) {
    this.itemForm = this.fb.group({
      itemName: this.fb.control<string>(!!s? s : '', 
          [ Validators.required, Validators.minLength(2) ]),
      price: this.fb.control<number>(0, [ Validators.required, Validators.min(0.01) ]),
      quantity: this.fb.control<number>(1, [ Validators.required, Validators.min(1) ])
    })
    this.addShare(this.currentBill.friends.length)
    this.itemArr.push(this.itemForm)
  }

  // Create share array and add number of shares  
  addShare(max: number) {
    for(let i = 0; i < max; i++) {
      this.itemForm.addControl(`share${i}`, this.fb.control<number>(0, [ Validators.required, Validators.min(0) ]))
    }
  }

  equalShare(i: number) {
    for(let j = 0; j < this.currentBill.friends.length; j++) {
      this.itemArr.at(i).get(`share${j}`)?.patchValue(1)
    }
  }

  removeItem(i: number) {
    this.itemArr.removeAt(i)
  }

  exclude(s: string) {
    this.form.get(s)?.patchValue(0)
  }

  splitByPercent() {
    this.router.navigate(['/bill-percent'])
  }

  // Post bill to backend
  createSharesArr(c: any): number[] {
    const sharesArr: number[] = []
    for (let i = 0; i < this.currentBill.friends.length; i++) {
      sharesArr.push(c.get(`share${i}`).value)
    }
    return sharesArr
  }

  saveBill() {
    this.currentBill.service = this.form.value.service
    this.currentBill.tax = this.form.value.tax
    this.currentBill.items = this.itemArr.controls
        .map(control => ({
          itemName: control.value.itemName,
          price: control.value.price,
          quantity: control.value.quantity,
          shares: this.createSharesArr(control),
          percentShares: []
      }))
    console.info(this.currentBill)

    firstValueFrom(this.billSvc.postBillSplit(this.currentBill))
      .then(response => { 
        const billId = response.billId
        console.info(billId)
        this.router.navigate(['/settle', billId])
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

  noShares(i: number) {
    let shares = 0
    for (let j = 0; j < this.currentBill.friends.length; j++) {
      shares += this.itemArr.at(i).get(`share${j}`)?.value
    }
    return shares < 1 && this.itemArr.at(i).dirty
  }

  incorrectTotal() {
    return this.totalPrice < (this.currentBill.total - 0.01) || 
        this.totalPrice > (this.currentBill.total + 0.01)
  }

  invalidForm() {
    let rowsWithNoShares = 0
    for (let i = 0; i < this.itemArr.length; i++) {
      if (this.noShares(i)) rowsWithNoShares++
    }
    return this.form.invalid || this.incorrectTotal() || rowsWithNoShares > 0
  }

  // Go to receipt-snap component:
  addReceipt() {
    this.billSvc.bill = this.currentBill
    this.router.navigate(['/receipt'])
  }
}
