import { Injectable } from "@angular/core";
import { Order } from "../models";
import { BaseHttpService } from "./baseHttp.service";
import { HttpClient } from "@angular/common/http";

@Injectable()
export class OrderService extends BaseHttpService<Order>{

    constructor(override readonly http: HttpClient) {
        super(http);
      }

    override get entityName(): string {
        return "order";
    }
}