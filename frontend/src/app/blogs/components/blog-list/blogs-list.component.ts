import { Component, Input } from "@angular/core" 
import { BlogsService } from "../../blogs.service";
import { Store } from "@ngrx/store";
import { Blog } from "../../blogs.model";
import * as blogReducer from "../../blogs.reducer"
import { clearBlogs, getBlogsRequested } from "../../blogs.actions";
import { selectAllBlogs, selectFetched, selectPages } from "../../blogs.selector";
import { getLoggedInUserRequested } from "src/app/auth/auth.actions";
import { ActivatedRoute, Router } from "@angular/router";
import { Form, FormBuilder, FormGroup, Validators } from "@angular/forms";
import { MatInput } from "@angular/material/input";
import { MatFormField } from "@angular/material/form-field";
import { IntersectionDirective } from "src/app/app-intersection/app-intersection.directive";
import { MatProgressSpinner } from "@angular/material/progress-spinner";

@Component({
    selector:'app-blogs-list',
    templateUrl:'blogs-list.component.html',
    styleUrls:['blogs-list.component.scss'],
}) export class BlogsListComponent{
    requestType;string
    search:string;
    blogs$ = this.store.select(selectAllBlogs)
    searchForm:FormGroup
    page:number=0
    fetching=true
    pages$= this.store.select(selectPages)
    pages=0
    constructor(private readonly store:Store<blogReducer.State>, private readonly route:ActivatedRoute, private readonly formBuilder: FormBuilder, private readonly router:Router){
        this.searchForm = this.formBuilder.group({
            search:["", Validators.required]
        })
    }

    ngOnInit(){
        this.pages$.subscribe(pages =>{
            if(pages){
                this.pages=pages
            }
        })
        this.route.params.subscribe(params =>{
            this.page=0
            this.store.dispatch(clearBlogs())
            this.requestType=params['requestType']
            this.store.dispatch(getBlogsRequested({payload:{page:this.page,size:10, requestType:this.requestType}}))
            this.fetching=true
        })
        this.blogs$.subscribe(blogs =>{
            if(blogs.length>0){
                this.page++
            }
            this.fetching=false
        })
    }

    onSubmit(){
        if(this.searchForm.valid){
            this.store.dispatch(clearBlogs())
            this.search = this.searchForm.controls["search"].value
            this.store.dispatch(getBlogsRequested({payload:{page:0,size:10, requestType:this.requestType, search:this?.search}}))
        }
        
    }

    scrollEnd(){
     if(this.pages>this.page && this.fetching==false){
        this.fetching=true
        this.store.dispatch(getBlogsRequested({payload:{page:this.page,size:10, requestType:this.requestType}}))
     }
    }

    
}