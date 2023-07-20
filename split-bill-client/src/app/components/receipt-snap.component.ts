import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { WebcamImage } from 'ngx-webcam';
import { Subject } from 'rxjs';
import { ReceiptService } from '../receipt.service';

@Component({
  selector: 'app-receipt-snap',
  templateUrl: './receipt-snap.component.html',
  styleUrls: ['./receipt-snap.component.scss']
})
export class ReceiptSnapComponent {
  links = ['Step 1: Add Friends & Expenditure', 'Step 2: Add Items', 'Step 3: Split Bill']
  activeLink = this.links[1];
  
  router = inject(Router)
  receiptSvc = inject(ReceiptService)

  trigger$ = new Subject<void>()

  viewWidth = 0
  viewHeight = 0

  ngOnInit(): void {
    this.viewWidth = 480
    //window.innerWidth * 1
    this.viewHeight = window.innerHeight * 0.8
  }

  image(image: WebcamImage) {
    console.info('>>>image ', image)
    this.receiptSvc.photo = image.imageAsDataUrl
    this.router.navigate([ '/receipt/upload' ])
  }

  snap() {
    this.trigger$.next()
  }
}
