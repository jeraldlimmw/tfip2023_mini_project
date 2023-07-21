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
import { StartComponent } from './components/start.component';
import { AddItemsComponent } from './components/add-items.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from './material.module';
import { IconModule } from './icon.module';
import { EmailDialogComponent } from './components/email-dialog.component';

const appRoutes: Routes = [
  {path: '', component: AddFriendsComponent},
  {path: 'items', component: AddItemsComponent},
  {path: 'receipt', component: ReceiptSnapComponent},
  {path: 'receipt/upload', component: ReceiptUploadComponent},
  {path: 'bill-share', component: ShareBillComponent},
  {path: 'bill-percent', component: PercentBillComponent},
  {path: 'settle/:billId', component: SettleUpComponent},
  {path: 'start', component: StartComponent},
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
    ReceiptUploadComponent,
    StartComponent,
    AddItemsComponent,
    EmailDialogComponent
  ],
  imports: [
    BrowserModule, HttpClientModule, ReactiveFormsModule, WebcamModule,
    RouterModule.forRoot(appRoutes, {useHash : true}),
    BrowserAnimationsModule, MaterialModule, IconModule
  ],
  providers: [BillService, ReceiptService],
  bootstrap: [AppComponent]
})
export class AppModule { }
