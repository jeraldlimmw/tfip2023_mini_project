import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";
import { Bill, BillIdResponse, SentResponse, Settlement, StartData } from "./models";

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
        tax: 0,
        chatId: 0,
        user: {
            userId: -1,
            username: "",
            firstName: ""
        }
    }

    // send form data to backend to store in Mongo
    // also creates transactions and sends back billId
    postBillSplit(bill: Bill) {
        return this.http.post<BillIdResponse>('/bill/store', bill)
    }

    // get settlement details
    getSettlement(id: string) {
        const params = new HttpParams().set('id', id)
        return this.http.get<Settlement>("/bill/settlement", { params })
    }

    emailBill(id: string, recipient: string) {
        return this.http.post<SentResponse>(`/bill/email/${id}`, recipient)
    }

    telegramBill(id: string) {
        return this.http.post<SentResponse>('/bill/telegram', id)
    }

    startTelegramBill(qs: string) {
        const params = new HttpParams().set('qs', qs)
        return this.http.get<StartData>('/bill/start', { params })
    }

    resetBill() {
        this.bill = {
            title: "",
            total: 0,
            friends: [],
            paid: [],
            byPercentage: false,
            items: [],
            service: 0,
            tax: 0,
            chatId: 0,
            user: {
                userId: -1,
                username: "",
                firstName: ""
            }
        }
    }

}