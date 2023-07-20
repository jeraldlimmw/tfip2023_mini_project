import { Component, OnInit, inject } from '@angular/core';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { ReceiptService } from '../receipt.service';
import { BillService } from '../bill.service';

@Component({
  selector: 'app-receipt-upload',
  templateUrl: './receipt-upload.component.html',
  styleUrls: ['./receipt-upload.component.scss']
})
export class ReceiptUploadComponent implements OnInit{
  links = ['Step 1: Add Friends & Expenditure', 'Step 2: Add Items', 'Step 3: Split Bill']
  activeLink = this.links[1];
  
  router = inject(Router)
  billSvc = inject(BillService)
  receiptSvc = inject (ReceiptService)
  photo = ''
  
  ngOnInit(): void {
    // If there is no photo, return to snap component
    if(!this.receiptSvc.photo) {
      this.router.navigate(['/receipt'])
    }
    // retrieve photo from service
    this.photo = this.receiptSvc.photo
  }

  upload() {
    firstValueFrom(this.receiptSvc.postReceipt(this.photo))
      .then(result => {
        this.billSvc.bill.items = result.receiptItems
        //alert('posted')
        this.router.navigate([ '/bill-share' ])
      })
      .catch(err => {
        alert(JSON.stringify(err))
        this.router.navigate([ '/items' ])
      })
  }
}
