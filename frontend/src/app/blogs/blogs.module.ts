import { HttpClientModule } from "@angular/common/http";
import { NgModule } from "@angular/core"
import { BrowserModule } from "@angular/platform-browser";
import { AppRoutingModule } from "../app-routing.module";
import { BlogsService } from "./blogs.service";
import { BlogsListComponent } from "./components/blog-list/blogs-list.component";
import * as blogsReducer from "./blogs.reducer"
import { StoreModule } from "@ngrx/store";
import { EffectsModule } from "@ngrx/effects";
import { BlogsEffects } from "./blogs.effects";
import { BlogsListItemComponent } from "./components/blog-list/blog-list-item.component/blogs-list-item.component";
import { CUSTOM_ELEMENTS_SCHEMA } from "@angular/compiler";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input";
import { BlogPageComponent } from "./components/blog-page/blogs-page.component";
import { IntersectionDirective } from "../app-intersection/app-intersection.directive";
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { EditorModule } from "@tinymce/tinymce-angular";
import { BlogFormComponent } from "./components/blog-form/blog-form.component";

@NgModule({
    declarations:[
        BlogPageComponent,
        BlogsListComponent,
        BlogsListItemComponent,
        BlogFormComponent,
        IntersectionDirective
    ],
    imports:[
        HttpClientModule,
        BrowserModule,
        AppRoutingModule,
        StoreModule.forFeature("blogs", blogsReducer.getReducer),
        EffectsModule.forFeature(BlogsEffects),
        FormsModule,
        ReactiveFormsModule,
        MatFormFieldModule,
        MatInputModule,
        MatProgressSpinnerModule,
        EditorModule,

    ],
    providers:[
        BlogsService
    ],
    bootstrap:[BlogsListComponent]
}) export class BlogsModule{}