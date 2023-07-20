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
    chatId: number
    user: User
    title: string
    total: number
    friends: string[]
    paid: Paid[]
    byPercentage: boolean
    items: Item[]
    service: number
    tax: number
}

export interface ReceiptItems {
    receiptItems: Item[]
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
    timestamp: number
    message: string
}

export interface User {
    userId : number
    username: string
    firstName: string
}

export interface SentResponse {
    message: string
}