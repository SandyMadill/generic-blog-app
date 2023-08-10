import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { enviorment } from "../enviorment/enviorment";
import { User } from "./user.model";
import { map } from "rxjs";

@Injectable()
export class UserService {

    constructor(private readonly http:HttpClient){

    }

    getUserPage(page:number, size:number){
        return this.http.get<any>(`${enviorment.backendHost}/user/${page}/${size}`).pipe(map(response=>{
            return {users:response.content,pages:response.totalPages}
        }))

    }

    getUserById(id:Number){
        return this.http.get<User>(`${enviorment.backendHost}/user/${id}`)
    }

    
}