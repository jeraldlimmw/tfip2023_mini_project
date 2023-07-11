import { Component, OnInit, inject } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { BillService } from '../bill.service';

@Component({
  selector: 'app-add-friends',
  templateUrl: './add-friends.component.html',
  styleUrls: ['./add-friends.component.css']
})
export class AddFriendsComponent implements OnInit{

  fb = inject(FormBuilder)
  router = inject(Router)
  billSvc = inject(BillService)
  
  form!: FormGroup
  friendsArr!: FormArray
  totalAmount = 0

  ngOnInit(): void {
    this.form = this.createForm()
    this.addFriend('me')
    this.addFriend('friend1')
  }

  createForm() {
    this.friendsArr = this.fb.array([])
    return this.fb.group({
      title: this.fb.control<string>('', [ Validators.required ]),
      friends: this.friendsArr
    })
  }

  addFriend(s: string | null) {
    this.friendsArr.push(
      this.fb.group({
        name: this.fb.control<string>(!!s? s : '', 
            [ Validators.required, Validators.minLength(2), Validators.maxLength(50)]),
        amount: this.fb.control<number>(0, 
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
    this.router.navigate(['/bill-share'])
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
