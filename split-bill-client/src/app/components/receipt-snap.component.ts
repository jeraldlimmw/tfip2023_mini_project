import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { WebcamImage } from 'ngx-webcam';
import { Subject } from 'rxjs';
import { ReceiptService } from '../receipt.service';

@Component({
  selector: 'app-receipt-snap',
  templateUrl: './receipt-snap.component.html',
  styleUrls: ['./receipt-snap.component.css']
})
export class ReceiptSnapComponent {
  router = inject(Router)
  receiptSvc = inject(ReceiptService)

  trigger$ = new Subject<void>()

  viewWidth = 0
  viewHeight = 0

  ngOnInit(): void {
    this.viewWidth = window.innerWidth * 0.5
    this.viewHeight = window.innerHeight * 0.5
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
