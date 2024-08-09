import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { UserService } from '../services/userService';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-user-form',
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.css']
})
export class UserFormComponent implements OnInit {
  user: any = { name: '', email: '' };
  isEdit = false;
  errorMessage: string | null = null;

  constructor(
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEdit = true;
      this.userService.getUserById(+id).subscribe({
        next: (response) => {
          this.user = response.data;  
        },
        error: (error: HttpErrorResponse) => {
          this.handleError(error);
        }
      });
    }
  }

  saveUser(): void {
    this.errorMessage = null;
    if (this.isEdit) {
      this.userService.updateUser(this.user.id, this.user).subscribe({
        next: () => {
          this.router.navigate(['/users']);
        },
        error: (error: HttpErrorResponse) => {
          this.handleError(error);
        }
      });
    } else {
      this.userService.createUser(this.user).subscribe({
        next: () => {
          this.router.navigate(['/users']);
        },
        error: (error: HttpErrorResponse) => {
          this.handleError(error);
        }
      });
    }
  }

  handleError(error: HttpErrorResponse): void {
    if (error.error && error.error.message) {
      this.errorMessage = error.error.message;
    } else {
      this.errorMessage = 'An unexpected error occurred. Please try again later.';
    }
  }

  goBack(): void {
    this.router.navigate(['/users']);
  }
}
