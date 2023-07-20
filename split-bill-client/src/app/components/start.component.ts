import { Component, OnInit, inject } from '@angular/core';
import { BillService } from '../bill.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-start',
  templateUrl: './start.component.html',
  styleUrls: ['./start.component.css']
})
export class StartComponent implements OnInit{
  billSvc = inject(BillService)
  activatedRoute = inject(ActivatedRoute)
  router = inject(Router)

  ngOnInit(): void {
    this.billSvc.bill.chatId = this.activatedRoute.snapshot.queryParams['chat_id']
    this.billSvc.bill.user.userId = this.activatedRoute.snapshot.queryParams['user_id']
    this.billSvc.bill.user.username = this.activatedRoute.snapshot.queryParams['username']
    this.billSvc.bill.user.firstName = this.activatedRoute.snapshot.queryParams['firstName']
    console.info(this.billSvc.bill)

    this.router.navigate(['/']);
  }
}
