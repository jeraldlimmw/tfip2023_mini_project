import { Component, OnInit, inject } from '@angular/core';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { ReceiptService } from '../receipt.service';
import { BillService } from '../bill.service';

@Component({
  selector: 'app-receipt-upload',
  templateUrl: './receipt-upload.component.html',
  styleUrls: ['./receipt-upload.component.css']
})
export class ReceiptUploadComponent implements OnInit{
  router = inject(Router)
  billSvc = inject(BillService)
  receiptSvc = inject (ReceiptService)
  photo = ''
  
  ngOnInit(): void {
    // If there is no photo, return to snap component
    if(!this.receiptSvc.photo) {
      this.router.navigate(['/'])
    }
    // retrieve photo from service
    this.photo = this.receiptSvc.photo
  }

  upload() {
    //var file = this.dataURLtoFile(this.photo, new Date().toString() + '.jpeg');
    // var file = this.dataURLtoFile(this.photo, 'receipt.jpeg');
    // console.info('>>> file: ', file)

    //firstValueFrom(this.receiptSvc.postReceipt(file))
    firstValueFrom(this.receiptSvc.postReceipt(this.photo))
      .then(result => {
        this.billSvc.bill.items = result.receiptItems
        if (result.taxAndServiceIncl) {
          this.billSvc.bill.tax = 0
          this.billSvc.bill.service = 0
        }
        alert('posted')
        this.router.navigate([ '/bill-share' ])
      })
      .catch(err => {
        alert(JSON.stringify(err))
      })
  }

  dataURLtoFile(dataurl: string, filename: string) {
    const regex = new RegExp(':(.*?);')
    
    var arr = dataurl.split(','),
        mime = arr[0].match(regex)![1],
        bstr = atob(arr[arr.length - 1]), 
        n = bstr.length, 
        u8arr = new Uint8Array(n);
    while(n--){
        u8arr[n] = bstr.charCodeAt(n);
    }
    return new File([u8arr], filename, {type:mime});
  }
}
