import { Component } from '@angular/core'
import { FormBuilder, Validators } from '@angular/forms';
import { ActionsSubject, Store, on } from '@ngrx/store';
import * as blogReducer from '../../blogs.reducer'
import { postBlogRequested, postBlogSucceeded, postThumbnailRequested, postThumbnailSucceeded } from '../../blogs.actions';
import { Router } from '@angular/router';

@Component({
    selector:'app-blog-form',
    templateUrl:'blog-form.component.html',
    styleUrls:['blog-form.component.scss']
}) export class BlogFormComponent{

    blogForm = this.formBuilder.group({
        title:['', Validators.required],
        blogText:['',Validators.required]
    })
    imageStatus:String
    id:number
    uploadedThumbnail:File
    fileByteArray:any[]

    constructor(private readonly store:Store<blogReducer.State>, private readonly formBuilder:FormBuilder, private readonly action:ActionsSubject, private router: Router){

    }

    ngOnInit(){
        this.action.subscribe(action =>{
            if(action.type==postBlogSucceeded.type){
                const a:any = action
                this.id=a.payload.id
                if(this.uploadedThumbnail!=null){
                    this.store.dispatch(postThumbnailRequested({payload:{file:this.uploadedThumbnail,id:this.id}}))
                }
                else
                {
                    this.router.navigate([`blog/${this.id}`])
                }
            }
            else if (action.type==postThumbnailSucceeded.type){
                this.router.navigate([`blog/${this.id}`])
            }
        })
    }

    onSubmit(){
        if(this.blogForm.valid){
            const blog = {title:this.blogForm.controls.title.value, blogText:this.blogForm.controls.blogText.value}
            this.store.dispatch(postBlogRequested({payload:blog}))
        }
    }


    processFile(imageInput: any) {
        const file:File = imageInput.files[0]
        const img = new Image();
        img.src = URL.createObjectURL(file);
        img.onload = e => {
            if(!(file.type=="image/png" || file.type=="image/jpeg")){
                this.imageStatus = "the thumbnail must be a png or jpeg"
                this.uploadedThumbnail=null
                this.fileByteArray=null
            }
            else if(img.width > 1000 || img.height > 1000){
                this.imageStatus = ("the image must have a height and width less than 1000 pixels")
                this.uploadedThumbnail=null
                this.fileByteArray=null
            }
            else
            {
                let reader = new FileReader();
                let fileByteArray = [];
                reader.readAsArrayBuffer(file);
                reader.onloadend = (evt) => {
                    if (evt.target.readyState === FileReader.DONE) {
                      const arrayBuffer = <ArrayBuffer>evt.target.result;
                      const array = new Uint8Array(arrayBuffer);
                      array.forEach((item) => fileByteArray.push(item));
                    }

                    this.imageStatus = ("valid image")
                    this.uploadedThumbnail=file
                    this.fileByteArray=fileByteArray
                }
            }
        }

    }

}