import { Component, OnInit, inject } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { BillService } from '../bill.service';
import { Bill } from '../models';

@Component({
  selector: 'app-add-friends',
  templateUrl: './add-friends.component.html',
  styleUrls: ['./add-friends.component.scss']
})
export class AddFriendsComponent implements OnInit{

  links = ['Step 1: Add Friends & Expenditure', 'Step 2: Add Items', 'Step 3: Split Bill']
  activeLink = this.links[0];

  fb = inject(FormBuilder)
  router = inject(Router)
  billSvc = inject(BillService)
  
  form!: FormGroup
  friendsArr!: FormArray
  currentBill!: Bill
  totalAmount = 0

  ngOnInit(): void {
    this.currentBill = this.billSvc.bill
    this.form = this.createForm()

    if (this.currentBill.friends.length > 0) {
      this.currentBill.friends.forEach(f => {
        let amountPaid = 0
        this.currentBill.paid.forEach(p => {
          if (p.name === f) amountPaid = p.amount
        }); 
        this.addFriend(f, amountPaid)
      });
      this.totalAmount = this.currentBill.total
    } else {
      const name = (!!this.currentBill.user.firstName) ? 
          this.currentBill.user.firstName : "me"
      this.addFriend(name, 0)
      this.addFriend('friend1', 0)
    }
  }

  createForm() {
    this.friendsArr = this.fb.array([])
    return this.fb.group({
      title: this.fb.control<string>(!!this.currentBill.title? this.currentBill.title : '', [ Validators.required ]),
      friends: this.friendsArr
    })
  }

  addFriend(s: string | null, a: number | null) {
    this.friendsArr.push(
      this.fb.group({
        name: this.fb.control<string>(!!s? s : '', 
            [ Validators.required, Validators.minLength(2), Validators.maxLength(50)]),
        amount: this.fb.control<number>(!!a? a: 0, 
            [ Validators.required, Validators.min(0) ])
      })
    )
  }

  removeFriend(i: number) {
    this.friendsArr.removeAt(i)
  }

  saveFriends() {
    this.billSvc.bill.title = this.form.value.title
    this.billSvc.bill.total = this.totalAmount
    this.billSvc.bill.friends = this.friendsArr.controls.map(
        control => control.value.name
    )
    this.billSvc.bill.paid = this.friendsArr.controls
        .filter(control => control.value.amount > 0)
        .map(control => ({
            name: control.value.name,
            amount: control.value.amount
        }))
    console.info(this.billSvc.bill)
    this.router.navigate(['/items'])
  }

  calculateTotalAmount() {
    this.totalAmount = 0
    for (let i = 0; i < this.friendsArr.length; i++) {
      this.totalAmount += this.friendsArr.at(i).get('amount')?.value
    }
  }

  // Validations
  lessThanThreeFriends() {
    return this.friendsArr.length < 3
  }
  
  invalidField(ctrlName: string) {
    return !!(this.form.get(ctrlName)?.invalid && this.form.get(ctrlName)?.dirty)
  }

  invalidFriendName(i: number, name: string) {
    return !!(this.friendsArr.at(i).get(name)?.invalid 
        && this.friendsArr.at(i).get(name)?.dirty)
  }

  invalidForm() {
    return this.totalAmount < 1 || this.form.invalid
  }
}
