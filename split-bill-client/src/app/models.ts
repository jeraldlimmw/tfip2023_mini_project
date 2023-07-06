export interface Item {
    itemName: string
    price: number
    quantity: number
    share: number[]
    percentShare: number[]
    // people: string[]
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

export interface Transaction {
    transactionId: string
    payer: string
    payee: string
    amount: number
    // paylahQR: string
}

export interface Settlement {
    billId: string
    title: string
    transactions: Transaction[]
    timestamp: number
}