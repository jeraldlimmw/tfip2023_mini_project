import { HttpClient } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";
import { Bill, BillIdResponse, Settlement } from "./models";

@Injectable()
export class BillService{

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
    // also creates transactions and sends back billId
    postBillSplit(bill: Bill) {
        return this.http.post<BillIdResponse>('/bill/store', bill)
    }

    // get settlement details
    getSettlement(id: string) {
        return this.http.get<Settlement>(`/bill/settlement/${id}`)
    }

}