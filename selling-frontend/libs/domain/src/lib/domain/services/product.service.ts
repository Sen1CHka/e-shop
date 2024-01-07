import { Injectable } from "@angular/core";
import { Product } from "../models";
import { BaseHttpService } from "./baseHttp.service";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

@Injectable({providedIn:"root"})
export class ProductService extends BaseHttpService<Product>{

    constructor(readonly client: HttpClient){
        super(client);
    }

    override get entityName(): string {
        return "product";
    }

    filterAll(filter?: any): Observable<Product[]> {
        return this.http.get<Product[]>(this.basePath, { params: filter });
    }

}