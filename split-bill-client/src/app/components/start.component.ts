import { Component, OnInit, inject } from '@angular/core';
import { BillService } from '../bill.service';
import { ActivatedRoute, Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { MatSnackBar } from '@angular/material/snack-bar';

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
    const query = this.activatedRoute.snapshot.queryParams['qs']
    console.info(query)

    firstValueFrom(this.billSvc.startTelegramBill(query))
      .then(data => { console.info(data)
        // if (!data.user || !data.chatId)
        this.billSvc.bill.chatId = data.chatId
        this.billSvc.bill.user = data.user
        console.info(this.billSvc.bill)
        this.router.navigate(['/']);
      })
      .catch(err => {
        this.openSnackBar("Could not retrieve data from Telegram", "Close")
        this.router.navigate(['/']);
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
