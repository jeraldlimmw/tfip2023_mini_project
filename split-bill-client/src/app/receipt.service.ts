import { HttpClient } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";
import { ReceiptItems } from "./models";

@Injectable()
export class ReceiptService {
    http = inject(HttpClient)
    photo = '';

    postReceipt(dataUrl: String) {
        return this.http.post<ReceiptItems>('/receipt/upload', dataUrl)
    }
}