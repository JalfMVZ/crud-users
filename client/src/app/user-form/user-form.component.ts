import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { UserService } from '../services/userService';

@Component({
  selector: 'app-user-form',
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.css']
})
export class UserFormComponent implements OnInit {
  user: any = { name: '', email: '', username: '', password: '' };
  isEdit = false;
  passwordFieldType: string = 'password';  // Inicialmente oculto

  constructor(
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEdit = true;
      this.userService.getUserById(+id).subscribe(data => {
        this.user = data;
      });
    }
  }

  saveUser(): void {
    if (this.isEdit) {
      this.userService.updateUser(this.user.id, this.user).subscribe(() => {
        this.router.navigate(['/users']);
      });
    } else {
      this.userService.createUser(this.user).subscribe(() => {
        this.router.navigate(['/users']);
      });
    }
  }

  goBack(): void {
    this.router.navigate(['/users']);
  }

  togglePassword(): void {
    this.passwordFieldType = this.passwordFieldType === 'password' ? 'text' : 'password';
  }
}
