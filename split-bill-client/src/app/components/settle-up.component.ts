import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Settlement } from '../models';
import { BillService } from '../bill.service';
import { firstValueFrom, lastValueFrom } from 'rxjs';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { EmailDialogComponent } from './email-dialog.component';

@Component({
  selector: 'app-settle-up',
  templateUrl: './settle-up.component.html',
  styleUrls: ['./settle-up.component.scss']
})
export class SettleUpComponent implements OnInit{
  router = inject(Router)
  activatedRoute = inject(ActivatedRoute)
  billSvc = inject(BillService)
  
  // canShare = false
  settlement!: Settlement
  email!: string
  id!: string
  date = ""

  ngOnInit(): void {
    this.id = this.activatedRoute.snapshot.params['billId']
    // this.canShare = !!navigator.share
    
    lastValueFrom(this.billSvc.getSettlement(this.id))
        .then(result => { 
          this.settlement = result
          console.info(this.settlement)
          this.date = this.dateToString(this.settlement.timestamp)
          this.billSvc.resetBill()
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

  sendEmail(id: string, email: string) {
    firstValueFrom(this.billSvc.emailBill(id, email))
      .then(result => {
        this.openSnackBar(`Full bill sent to ${email}`, 'Close')
      })
      .catch(err => {
        alert(JSON.stringify(err))
      })
  }

  share() {
    const data: any = {
      text: this.settlement.message
    }

    navigator.share(data)
      .then(result => {
        this.openSnackBar(`Settlement shared`, 'Close')
      })
      .catch(err => {
        alert(JSON.stringify(err))
      })  
  }

  // Mat dialog
  dialog = inject(MatDialog)

  openDialog() {    
    const dialogConfig = new MatDialogConfig()
    
    dialogConfig.disableClose = false;
    dialogConfig.autoFocus = true;
    dialogConfig.data = {
      email: ""
    }
    const dialogRef = this.dialog.open(EmailDialogComponent, dialogConfig)

    dialogRef.afterClosed().subscribe(data => {
      console.log(data.email)
      this.sendEmail(this.id, data.email)
    })
  }

  // Mat snack bar notification
  _snackBar = inject(MatSnackBar)

  openSnackBar(message: string, action: string) {
    this._snackBar.open(message, action, {
      duration: 3000
    });
  }
}