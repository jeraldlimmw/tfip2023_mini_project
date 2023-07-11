export interface Item {
    itemName: string
    price: number
    quantity: number
    shares: number[]
    percentShares: number[]
}

export interface Paid {
    name: string
    amount: number
}

export interface Bill {
    title: string
    total: number
    friends: string[]
    paid: Paid[]
    byPercentage: boolean
    items: Item[]
    service: number
    tax: number
}

export interface ReceiptResponse {
    receiptItems: Item[]
    receiptTotal: number
    taxAndServiceIncl: boolean
}

export interface BillIdResponse {
    billId: string
}

export interface Transaction {
    transactionId: string
    payer: string
    payee: string
    amount: number
}

export interface Settlement {
    billId: string
    total: number
    title: string
    transactions: Transaction[]
    //message: string
    timestamp: number
}