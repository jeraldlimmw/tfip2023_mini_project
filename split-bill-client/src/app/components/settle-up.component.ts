import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Bill, Settlement } from '../models';
import { BillService } from '../bill.service';
import { lastValueFrom } from 'rxjs';

@Component({
  selector: 'app-settle-up',
  templateUrl: './settle-up.component.html',
  styleUrls: ['./settle-up.component.css']
})
export class SettleUpComponent implements OnInit{
  router = inject(Router)
  activatedRoute = inject(ActivatedRoute)
  billSvc = inject(BillService)
  
  settlement!: Settlement
  date = ""

  ngOnInit(): void {
    const billId = this.activatedRoute.snapshot.params['billId']

    lastValueFrom(this.billSvc.getSettlement(billId))
        .then(result => { 
          this.settlement = result
          console.info(this.settlement)
          this.date = this.dateToString(this.settlement.timestamp)
        })
        .catch(err => alert(err.error))
  }

  dateToString(timestamp: number): string {
    const options: Intl.DateTimeFormatOptions = {
      weekday: 'long',
      day: 'numeric',
      month: 'short',
      year: 'numeric',
      hour: 'numeric',
      minute: 'numeric',
      second: 'numeric',
      timeZone: 'Asia/Singapore'
    }
    const date = new Date(timestamp)
    return date.toLocaleString('en-GB', options)
  }

}
