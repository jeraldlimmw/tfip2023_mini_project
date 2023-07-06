import { Component, OnInit, inject } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UploadService } from '../upload.service';
import { Router } from '@angular/router';
import { Bill, Item } from '../models';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-bill',
  templateUrl: './bill.component.html',
  styleUrls: ['./bill.component.css']
})
export class BillComponent implements OnInit{

  fb = inject(FormBuilder)
  router = inject(Router)
  uploadSvc = inject(UploadService)

  form!: FormGroup
  itemArr!: FormArray
  itemForm!: FormGroup
  currentBill!: Bill
  formTotal = 0

  ngOnInit(): void {
    this.currentBill = this.uploadSvc.bill
    this.form = this.createForm()
  }

  createForm() {
    this.itemArr = this.fb.array([])
    this.addItem('item')
    return this.fb.group({
      items: this.itemArr,
      service: this.fb.control<number>(10, [ Validators.required, Validators.min(0) ]),
      tax: this.fb.control<number>(8, [ Validators.required, Validators.min(0) ])
    })
  }

  // add and remote item from ItemArr
  addItem(s: string | null) {
    this.itemForm = this.fb.group({
      itemName: this.fb.control<string>(!!s? s : '', 
          [ Validators.required, Validators.minLength(2) ]),
      price: this.fb.control<number>(0, [ Validators.required, Validators.min(0.01) ]),
      quantity: this.fb.control<number>(1, [ Validators.required, Validators.min(1) ])
    })
    this.addCheckboxes(this.currentBill.friends.length)
    this.itemArr.push(this.itemForm)
  }
  
  addCheckboxes(max: number) {
    for(let i = 0; i < max; i++) {
      this.itemForm.addControl(`p${i}`, this.fb.control<string>(''))
    }
  }

  removeItem(i: number) {
    this.itemArr.removeAt(i)
  }

  // on some change, recalculate the bill total so that it matches the value previously keyed in

  // post bill to backend
  saveBill() {
    this.currentBill.service = this.form.value.service
    this.currentBill.tax = this.form.value.tax
    this.currentBill.items = this.itemArr.controls
        .map(control => ({
          itemName: control.value.itemName,
          price: control.value.price,
          quantity: control.value.quantity,
          people: this.createPeopleArr(control)
      }))
    console.info(this.currentBill)
    // must add promise
    firstValueFrom(this.uploadSvc.postBillSplit(this.currentBill))
      .then(result => {
        console.info(result)
      })
      .catch(err => {
        alert(JSON.stringify(err))
      })
  }

  createPeopleArr(c: any): string[] {
    const pplArr: string[] = []
    for (let i = 0; i < this.currentBill.friends.length; i++) {
      if (c.get(`p${i}`).value)
          pplArr.push(this.currentBill.friends[i])
    }
    return pplArr
  }
}
