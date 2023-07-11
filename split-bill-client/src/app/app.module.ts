import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http'
import { WebcamModule } from 'ngx-webcam'
import { ReactiveFormsModule } from '@angular/forms'
import { Routes, RouterModule } from '@angular/router'

import { AppComponent } from './app.component';
import { AddFriendsComponent } from './components/add-friends.component';
import { ShareBillComponent } from './components/share-bill.component';
import { SettleUpComponent } from './components/settle-up.component';
import { BillService } from './bill.service';
import { ReceiptService } from './receipt.service';
import { PercentBillComponent } from './components/percent-bill.component';
import { ReceiptSnapComponent } from './components/receipt-snap.component';
import { ReceiptUploadComponent } from './components/receipt-upload.component';

const appRoutes: Routes = [
  {path: '', component: AddFriendsComponent},
  {path: 'receipt', component: ReceiptSnapComponent},
  {path: 'receipt/upload', component: ReceiptUploadComponent},
  {path: 'bill-share', component: ShareBillComponent},
  {path: 'bill-percent', component: PercentBillComponent},
  {path: 'settle/:billId', component: SettleUpComponent},
  {path: '**', redirectTo: '/', pathMatch:'full'} 
]

@NgModule({
  declarations: [
    AppComponent,
    AddFriendsComponent,
    ShareBillComponent,
    SettleUpComponent,
    PercentBillComponent,
    ReceiptSnapComponent,
    ReceiptUploadComponent
  ],
  imports: [
    BrowserModule, HttpClientModule, ReactiveFormsModule, WebcamModule,
    RouterModule.forRoot(appRoutes, {useHash : true})
  ],
  providers: [BillService, ReceiptService],
  bootstrap: [AppComponent]
})
export class AppModule { }
