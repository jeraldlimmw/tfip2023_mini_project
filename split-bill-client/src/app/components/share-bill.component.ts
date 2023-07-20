import { Component, OnInit, inject } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BillService } from '../bill.service';
import { Router } from '@angular/router';
import { Bill, Item } from '../models';
import { firstValueFrom } from 'rxjs';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-bill',
  templateUrl: './share-bill.component.html',
  styleUrls: ['./share-bill.component.scss']
})
export class ShareBillComponent implements OnInit{
  
  links = ['Step 1: Add Friends & Expenditure', 'Step 2: Add Items', 'Step 3: Split Bill']
  activeLink = this.links[2];
  // splitBy = ['share', 'percent']
  // activeSplitBy = this.splitBy[0]
  
  fb = inject(FormBuilder)
  router = inject(Router)
  billSvc = inject(BillService)

  form!: FormGroup
  shareArr!: FormArray
  shareForm!: FormGroup
  currentBill!: Bill

  ngOnInit(): void {
    this.currentBill = this.billSvc.bill
    this.currentBill.byPercentage = false
    this.form = this.createForm()
  }

  // Create forms and arrays
  createForm() {
    this.shareArr = this.fb.array([])
    this.currentBill.items.forEach(item => {
      this.addShareControls()
    });
    return this.fb.group({
      shares: this.shareArr
    })
  }

  addShareControls() {
    this.shareForm = this.fb.group({})
    for(let j = 0; j < this.currentBill.friends.length; j++) {
      this.shareForm.addControl(`share${j}`, this.fb.control<number>(0, [ Validators.required, Validators.min(0) ]))
    }
    this.shareArr.push(this.shareForm)
  }

  equalShare(i: number) {
    for(let j = 0; j < this.currentBill.friends.length; j++) {
      this.shareArr.at(i).get(`share${j}`)?.patchValue(1)
    }
  }

  splitByPercent() {
    this.billSvc.bill = this.currentBill
    this.router.navigate(['/bill-percent'])
  }

  // Post bill to backend
  getSharesArr(i: number): number[] {
    const shares: number[] = []
    for (let j = 0; j < this.currentBill.friends.length; j++) {
      shares.push(this.shareArr.at(i).get(`share${j}`)?.value)
    }
    return shares
  }

  // Mat snack bar notification
  constructor(private _snackBar: MatSnackBar) {}

  openSnackBar(message: string, action: string) {
    this._snackBar.open(message, action, {
      duration: 3000
    });
  }

  saveBill() {
    for(let i = 0; i < this.currentBill.items.length; i++) {
      this.currentBill.items[i].shares = this.getSharesArr(i)
    }
    console.info(this.currentBill)

    firstValueFrom(this.billSvc.postBillSplit(this.currentBill))
      .then(response => { 
        const billId = response.billId
        console.info(billId)

        if (this.billSvc.bill.chatId != -1) {
          firstValueFrom(this.billSvc.telegramBill(billId))
            .then(result => {
              this.openSnackBar('Settlement sent to Telegram chat', 'Close')
            })
            .catch(error => {})
        }
        this.router.navigate(['/settle', billId])
      })
      .catch(err => {
        alert(JSON.stringify(err))
      })
  }

  // Validations
  noShares(i: number) {
    let shares = 0
    for (let j = 0; j < this.currentBill.friends.length; j++) {
      shares += this.shareArr.at(i).get(`share${j}`)?.value
    }
    return shares < 1
  }

  invalidForm() {
    let rowsWithNoShares = 0
    for (let i = 0; i < this.shareArr.length; i++) {
      if (this.noShares(i)) rowsWithNoShares++
    }
    return this.form.invalid || rowsWithNoShares > 0
  }
}
