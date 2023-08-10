import { Component } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { DatePipe } from "@angular/common";
import { State, Store } from "@ngrx/store";
import * as userReducer from '../user.reducer'
import { getUserByIdRequested } from "../user.actions";
import { selectFetched, selectUserEntites } from "../user.selector";
import { combineLatest } from "rxjs";
import { clearBlogs, getBlogsForUserRequested, getBlogsRequested } from "src/app/blogs/blogs.actions";
import { Blog } from "src/app/blogs/blogs.model";
import { selectAllBlogs } from "src/app/blogs/blogs.selector";
import { HttpClient } from "@angular/common/http";
import { enviorment } from "src/app/enviorment/enviorment";
import { DomSanitizer, Title } from "@angular/platform-browser";
import { User } from "../user.model";

@Component({
    selector:'app-user',
    templateUrl:'user.component.html',
    styleUrls:['user.component.scss']
})
export class UserComponent{

    userId:number
    userEntities$ = this.store.select(selectUserEntites)
    user:User
    blogs$ = this.store.select(selectAllBlogs)
    fetched$ = this.store.select(selectFetched)
    monthBlogs = new Map<string, {blogs:Blog[],reveal:boolean}>();
    profilePicture:any
    
    constructor(private readonly store:Store<userReducer.State>, private readonly route:ActivatedRoute, private readonly router:Router, private readonly http:HttpClient, private sanitizer: DomSanitizer, private readonly title:Title){
    }

    ngOnInit(){
        this.store.dispatch(clearBlogs())
        combineLatest([this.route.params,this.userEntities$,this.fetched$]).subscribe(([params, userEntities, fetched]) =>{
            this.userId= params["id"]
            this.store.dispatch(getBlogsForUserRequested({payload:this.userId}))
            if(fetched && !userEntities[this.userId]){
                this.store.dispatch(getUserByIdRequested({payload:params["id"]}))
            }
            else if (fetched){
                this.user = userEntities[this.userId]
                this.title.setTitle(this.user.username)
            }
        })

        this.blogs$.subscribe(blogs => {
            blogs.map(blog => {
                const key = new Date(blog.postedAt.toString()).toLocaleDateString("en-uk",{month:'long', year:'numeric'})
                if(this.monthBlogs.has(key)){
                    this.monthBlogs.get(key).blogs.push(blog)
                }
                else
                {
                    this.monthBlogs.set(key, {blogs:[blog],reveal:false})
                }
            })
        })

        this.http.get(`${enviorment.backendHost}/img/pfp/${this.userId}/400x400.png`, { responseType: 'blob' } ).subscribe(pfpResponse =>{
            let objectURL = URL.createObjectURL(pfpResponse);
             this.profilePicture = this.sanitizer.bypassSecurityTrustUrl(objectURL);
        })
    }

    revealMonth(key:string){
        this.monthBlogs.get(key).reveal = true
    }

    hideMonth(key:string){
        this.monthBlogs.get(key).reveal = false
    }

    selectBlog(id:number){
        this.router.navigate([`/blog/${id}`])
    }
}