import { Injectable } from "@angular/core";
import { Order } from "../models";
import { BaseHttpService } from "./baseHttp.service";
import { HttpClient } from "@angular/common/http";

@Injectable({
    providedIn: 'root',
})
export class OrderService extends BaseHttpService<Order>{

    constructor(readonly client: HttpClient){
        super(client);
    }

    override get entityName(): string {
        return "Order";
    }
}