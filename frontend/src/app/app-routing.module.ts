import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './auth/component/login/login.component';
import { LogoutComponent } from './auth/component/logout/logout.component';
import { BlogsListComponent } from './blogs/components/blog-list/blogs-list.component';
import { BlogPageComponent } from './blogs/components/blog-page/blogs-page.component';
import { BlogFormComponent } from './blogs/components/blog-form/blog-form.component';
import { RegisterComponent } from './auth/component/register/register.component';
import { UserComponent } from './user/component/user.component';
import { UserListComponent } from './user/component/user-list/user-list.component';
import { UserSettingsComponent } from './user/component/user-settings/user-settings.component';

const routes: Routes = [
  {path:"login", component:LoginComponent},
  {path:"logout", component:LogoutComponent},
  {path:"register", component:RegisterComponent},
  {path:"blogs/:requestType", component:BlogsListComponent},
  {path:"blog/:id", component:BlogPageComponent},
  {path:"upload", component:BlogFormComponent},
  {path:"user/:id", component:UserComponent},
  {path:"users" ,component:UserListComponent},
  {path:"user-settings" ,component:UserSettingsComponent}
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
