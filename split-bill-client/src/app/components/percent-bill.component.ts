import { Component, inject } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { Bill } from '../models';
import { BillService } from '../bill.service';

@Component({
  selector: 'app-percent-bill',
  templateUrl: './percent-bill.component.html',
  styleUrls: ['./percent-bill.component.scss']
})
export class PercentBillComponent {

  links = ['Step 1: Add Friends & Expenditure', 'Step 2: Add Items', 'Step 3: Split Bill']
  activeLink = this.links[2];
  // splitBy = ['share', 'percent']
  // activeSplitBy = this.splitBy[1]

  fb = inject(FormBuilder)
  router = inject(Router)
  billSvc = inject(BillService)

  form!: FormGroup
  percentArr!: FormArray
  percentForm!: FormGroup
  currentBill!: Bill

  ngOnInit(): void {
    this.billSvc.bill.byPercentage = true
    this.currentBill = this.billSvc.bill
    this.form = this.createForm()
  }

  // Create form
  createForm() {
    this.percentArr = this.fb.array([])
    this.currentBill.items.forEach(item => {
      this.addPercentControls()
    })
    return this.fb.group({
      percentShares: this.percentArr
    })
  }

  addPercentControls() {
    this.percentForm = this.fb.group({})
    for(let j = 0; j < this.currentBill.friends.length; j++) {
      this.percentForm.addControl(`percent${j}`, this.fb.control<number>(0, [ Validators.required, Validators.min(0) ]))
    }
    this.percentArr.push(this.percentForm)
  }

  splitByShare() {
    this.billSvc.bill = this.currentBill
    this.router.navigate(['/bill-share'])
  }

  // Post bill to backend
  getPercentSharesArr(i: number): number[] {
    const percentShares: number[] = []
    for (let j = 0; j < this.currentBill.friends.length; j++) {
      percentShares.push(this.percentArr.at(i).get(`percent${j}`)?.value / 100)
    }
    return percentShares
  }

  saveBill() {
    for(let i = 0; i < this.currentBill.items.length; i++) {
      this.currentBill.items[i].percentShares = this.getPercentSharesArr(i)
    }
    console.info(this.currentBill)
    
    firstValueFrom(this.billSvc.postBillSplit(this.currentBill))
    .then(response => { 
      const billId = response.billId
      console.info(billId)

      if (this.billSvc.bill.chatId != -1) {
        firstValueFrom(this.billSvc.telegramBill(billId))
          .then(result => {})
          .catch(error => {})
      }
      this.router.navigate(['/settle', billId])
    })
    .catch(err => {
      alert(JSON.stringify(err))
    })
  }

  // Validations
  not100Percent(i: number) {
    let totalPercentage = 0
    for (let j = 0; j < this.currentBill.friends.length; j++) {
      totalPercentage += this.percentArr.at(i).get(`percent${j}`)?.value
    }
    return totalPercentage != 100
  }

  invalidForm() {
    let rowsWithout100Percent = 0
    for (let i = 0; i < this.percentArr.length; i++) {
      if (this.not100Percent(i)) rowsWithout100Percent++
    }
    return this.form.invalid || rowsWithout100Percent > 0
  }
}
