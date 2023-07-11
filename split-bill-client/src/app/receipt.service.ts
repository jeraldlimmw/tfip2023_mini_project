import { HttpClient } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";
import { ReceiptResponse } from "./models";

@Injectable()
export class ReceiptService {
    http = inject(HttpClient)
    photo = '';

    postReceipt(dataUrl: String) {
        // const formData = new FormData()
        // formData.set('file', file) // @RequestPart MultipartFile myFile
        
        return this.http.post<ReceiptResponse>('/receipt/upload', dataUrl)
    }
}