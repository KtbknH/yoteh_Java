import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environments';
import { 
  User, 
  LoginRequest, 
  RegisterRequest, 
  AuthResponse 
} from '../../shared/models/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);
  
  private apiUrl = `${environment.apiUrl}/auth`;
  
  // Signal pour l'utilisateur actuel (Angular 17+)
  currentUser = signal<User | null>(null);

  constructor() {
    const token = this.getToken();
    if (token) {
      // Charger l'utilisateur depuis le token ou le localStorage
      const userData = localStorage.getItem('user');
      if (userData) {
        this.currentUser.set(JSON.parse(userData));
      }
    }
  }

  /**
   * Inscription d'un nouvel utilisateur
   */
  register(request: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, request)
      .pipe(
        tap(response => this.setSession(response))
      );
  }

  /**
   * Connexion d'un utilisateur
   */
  login(request: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, request)
      .pipe(
        tap(response => this.setSession(response))
      );
  }

  /**
   * Déconnexion
   */
  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    this.currentUser.set(null);
    this.router.navigate(['/auth/login']);
  }

  /**
   * Sauvegarde de la session
   */
  private setSession(authResponse: AuthResponse): void {
    localStorage.setItem('token', authResponse.token);
    
    const user: User = {
      id: authResponse.userId,
      email: authResponse.email,
      firstName: authResponse.firstName,
      lastName: authResponse.lastName,
      role: authResponse.role,
      loyaltyPoints: authResponse.loyaltyPoints
    };
    
    localStorage.setItem('user', JSON.stringify(user));
    this.currentUser.set(user);
  }

  /**
   * Récupération du token
   */
  getToken(): string | null {
    return localStorage.getItem('token');
  }

  /**
   * Vérification de l'authentification
   */
  isAuthenticated(): boolean {
    const token = this.getToken();
    return !!token;
  }

  /**
   * Vérification du rôle admin
   */
  isAdmin(): boolean {
    const user = this.currentUser();
    return user?.role === 'ADMIN';
  }

  /**
   * Récupération de l'utilisateur actuel
   */
  getCurrentUser(): User | null {
    return this.currentUser();
  }
}
