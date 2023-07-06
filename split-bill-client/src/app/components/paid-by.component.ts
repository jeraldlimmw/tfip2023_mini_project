import { Component, OnInit, inject } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UploadService } from '../upload.service';
import { Bill, Paid } from '../models';

@Component({
  selector: 'app-paid-by',
  templateUrl: './paid-by.component.html',
  styleUrls: ['./paid-by.component.css']
})
export class PaidByComponent implements OnInit{

  fb = inject(FormBuilder)
  uploadSvc = inject(UploadService)

  paidByForm!: FormGroup
  paidByArr!: FormArray
  currentBill!: Bill
  amountArr!: number[]

  ngOnInit(): void {
    this.currentBill = this.uploadSvc.bill  
    this.paidByForm = this.createForm()
  }

  createForm() {
    this.paidByArr = this.fb.array([])
    this.addPaidBy(this.currentBill.total)
    for (let i = 1; i < this.currentBill.friends.length; i++) {
      this.addPaidBy(0)      
    }    
    return this.fb.group({
      paidBy: this.paidByArr
    })
  }

  addPaidBy(n: number | null) {
    this.paidByArr.push(
      this.fb.group({
        amount: this.fb.control<number>(!!n? n : 0.00, [ Validators.required, Validators.min(0) ])
      })
    )
  }

  savePaidBy() {
    this.amountArr = this.paidByArr.controls.map(control => control.value)
    for(let i = 0; i < this.amountArr.length; i++) {
      const paid: Paid = {
        name: this.currentBill.friends[i],
        amount: this.amountArr[i]
      }
      if(this.amountArr[i] > 0) 
        this.uploadSvc.bill.paid.push(paid)
    }
    console.info(this.uploadSvc.bill)
  }

  invalid() {
    // this.amountArr.forEach()
    return this.paidByForm.invalid
  }
}
