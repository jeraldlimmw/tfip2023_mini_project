import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http'
import { ReactiveFormsModule } from '@angular/forms'
import { Routes, RouterModule } from '@angular/router'

import { AppComponent } from './app.component';
import { AddFriendsComponent } from './components/add-friends.component';
import { BillComponent } from './components/bill.component';
import { PaidByComponent } from './components/paid-by.component';
import { SettleUpComponent } from './components/settle-up.component';
import { UploadService } from './upload.service';

const appRoutes: Routes = [
  {path: '', component: AddFriendsComponent},
  //{path: 'payment', component: PaidByComponent},
  {path: 'items', component: BillComponent},
  {path: 'settle', component: SettleUpComponent},
  {path: '**', redirectTo: '/', pathMatch:'full'} 
]

@NgModule({
  declarations: [
    AppComponent,
    AddFriendsComponent,
    BillComponent,
    PaidByComponent,
    SettleUpComponent
  ],
  imports: [
    BrowserModule, HttpClientModule, ReactiveFormsModule, 
    RouterModule.forRoot(appRoutes, {useHash : true})
  ],
  providers: [UploadService],
  bootstrap: [AppComponent]
})
export class AppModule { }
