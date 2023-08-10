import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { enviorment } from "../enviorment/enviorment";
import { User } from "../user/user.model";

@Injectable()
export class SubscriptionsService{

    constructor(private readonly http:HttpClient){

    }

    subscribeToUser(id:number){
        return this.http.put<User>(`${enviorment.backendHost}/auth/subscribe`,{id:id})
    }

    unsubscribeToUser(id:number){
        return this.http.put<User>(`${enviorment.backendHost}/auth/unsubscribe`,{id:id})
    }

    getSubscriptions(){
        return this.http.get<User[]>(`${enviorment.backendHost}/auth/subscriptions`)
    }
}