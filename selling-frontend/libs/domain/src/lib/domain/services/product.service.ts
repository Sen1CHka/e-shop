import { Injectable } from "@angular/core";
import { Product } from "../models";
import { BaseHttpService } from "./baseHttp.service";
import { HttpClient } from "@angular/common/http";

@Injectable({
    providedIn: "root"
})
@Injectable()
export class ProductService extends BaseHttpService<Product>{

    constructor(readonly client: HttpClient){
        super(client);
    }

    override get entityName(): string {
        return "product";
    }
}