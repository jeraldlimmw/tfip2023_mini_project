import { HttpClient } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";
import { Bill, Settlement } from "./models";

@Injectable()
export class UploadService{

    http = inject(HttpClient)

    bill: Bill = {
        title: "",
        total: 0,
        friends: [],
        paid: [],
        byPercentage: false,
        items: [],
        service: 0,
        tax: 0
    }

    // send form data to backend to store in Mongo
    postBillSplit(bill: Bill) {
        return this.http.post<string>('/bill', bill)
    }

    // get settlement details
    getSettlement(id: string) {
        return this.http.get<Settlement>(`/bill/${id}`)
    }

}