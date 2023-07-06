import { Component, OnInit, inject } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UploadService } from '../upload.service';
import { Bill, Paid } from '../models';

@Component({
  selector: 'app-add-friends',
  templateUrl: './add-friends.component.html',
  styleUrls: ['./add-friends.component.css']
})
export class AddFriendsComponent implements OnInit{

  fb = inject(FormBuilder)
  router = inject(Router)
  uploadSvc = inject(UploadService)
  
  form!: FormGroup
  friendsArr!: FormArray

  ngOnInit(): void {
    this.form = this.createForm()
    this.addFriend('me')
    this.addFriend('friend1')
  }

  createForm() {
    this.friendsArr = this.fb.array([])
    return this.fb.group({
      title: this.fb.control<string>('', [ Validators.required ]),
      total: this.fb.control<number>(0.00, [ Validators.required, Validators.min(0.01)]),
      friends: this.friendsArr
    })
  }

  addFriend(s: string | null) {
    this.friendsArr.push(
      this.fb.group({
        name: this.fb.control<string>(!!s? s : '', 
            [ Validators.required, Validators.minLength(2)]),
        amount: this.fb.control<number>(0, 
            [ Validators.required, Validators.min(0) ])
      })
    )
  }

  removeFriend(i: number) {
    this.friendsArr.removeAt(i)
  }

  saveFriends() {
    this.uploadSvc.bill.title = this.form.value.title
    this.uploadSvc.bill.total = this.form.value.total
    this.uploadSvc.bill.friends = this.friendsArr.controls.map(
        control => control.value.name
    )
    this.uploadSvc.bill.paid = this.friendsArr.controls
        .filter(control => control.value.amount > 0)
        .map(control => ({
            name: control.value.name,
            amount: control.value.amount
        }))
    console.info(this.uploadSvc.bill)
    this.router.navigate(['/items'])
  }
}
