import { Component } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { Store } from "@ngrx/store";
import * as userReducer from '../../user.reducer'
import { getUsersRequested } from "../../user.actions";
import { selectAllUsers, selectFetched, selectPages } from "../../user.selector";
import { Blog } from "src/app/blogs/blogs.model";
import { HttpClient } from "@angular/common/http";
import { enviorment } from "src/app/enviorment/enviorment";
import { DomSanitizer, SafeUrl } from "@angular/platform-browser";
import { selectSubscriptionsEntites } from "src/app/subscriptions/subscriptions.selector";
import { Dictionary } from "@ngrx/entity";
import { User } from "../../user.model";
import { subscribeToUserRequested, unsubscribeToUserRequested } from "src/app/subscriptions/subscriptions.actions";
import { MatProgressSpinner } from "@angular/material/progress-spinner";

@Component({
    selector:'app-user-list',
    templateUrl:'user-list.component.html',
    styleUrls:['user-list.component.scss']
})
export class UserListComponent{
    users$ = this.store.select(selectAllUsers)
    fetched$ = this.store.select(selectFetched)
    fetched=false
    fetching=true
    pages$= this.store.select(selectPages)
    page:number=0
    pages=0
    subscriptionEntities$=this.store.select(selectSubscriptionsEntites)
    subscriptionsEntities:Dictionary<User>
    pfps = new Map<number, SafeUrl>();
    
    constructor(private readonly store:Store<userReducer.State>, private readonly route:ActivatedRoute, private readonly router:Router, private readonly http:HttpClient, private sanitizer: DomSanitizer){
    }

    ngOnInit(){
        this.store.dispatch(getUsersRequested({payload:{page:0,size:10}}))
        this.fetched$.subscribe(fetched =>{
            this.fetched = fetched
        })

        this.users$.subscribe(users => {
            if(users.length>0){
                this.page++
            }
            this.fetching=false
            users.map(user => {
            this.http.get(`${enviorment.backendHost}/img/pfp/${user.id}/40x40.png`, { responseType: 'blob' } ).subscribe(pfpResponse =>{
                let objectURL = URL.createObjectURL(pfpResponse);
                this.pfps.set(user.id, this.sanitizer.bypassSecurityTrustUrl(objectURL))
            })
        })
        })
        
        this.subscriptionEntities$.subscribe(subscriptionsEntities => {
            this.subscriptionsEntities = subscriptionsEntities
        })

        this.pages$.subscribe(pages => this.pages = pages)
    }

    selectUser(id:number){
        this.router.navigate([`user/${id}`])
    }

    subscribe(id:number){
        this.store.dispatch(subscribeToUserRequested({payload:id}))
    }

    unsubscribe(id:number){
        this.store.dispatch(unsubscribeToUserRequested({payload:id}))
    }

    scrollEnd(){
        if(this.pages>this.page && this.fetching==false){
           this.fetching=true
           this.store.dispatch(getUsersRequested({payload:{page:this.page,size:10}}))
        }
    }
}