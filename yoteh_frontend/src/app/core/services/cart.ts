import { Injectable, signal, computed } from '@angular/core';
import { Product } from '../../shared/models/product.model';
import { CartItem } from '../../shared/models/cart.model';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private cartItems = signal<CartItem[]>([]);
  
  // Computed signals
  items = this.cartItems.asReadonly();
  itemCount = computed(() => 
    this.cartItems().reduce((count, item) => count + item.quantity, 0)
  );
  total = computed(() => 
    this.cartItems().reduce((total, item) => 
      total + (item.product.price * item.quantity), 0
    )
  );

  constructor() {
    // Charger le panier depuis localStorage
    const savedCart = localStorage.getItem('cart');
    if (savedCart) {
      this.cartItems.set(JSON.parse(savedCart));
    }
  }

  /**
   * Ajoute un produit au panier
   */
  addToCart(product: Product, quantity: number = 1): void {
    const currentItems = [...this.cartItems()];
    const existingItem = currentItems.find(item => 
      item.product.id === product.id
    );

    if (existingItem) {
      existingItem.quantity += quantity;
    } else {
      currentItems.push({ product, quantity });
    }

    this.updateCart(currentItems);
  }

  /**
   * Retire un produit du panier
   */
  removeFromCart(productId: number): void {
    const currentItems = this.cartItems().filter(item =>
      item.product.id !== productId
    );
    this.updateCart(currentItems);
  }

  /**
   * Met à jour la quantité d'un produit
   */
  updateQuantity(productId: number, quantity: number): void {
    const currentItems = [...this.cartItems()];
    const item = currentItems.find(i => i.product.id === productId);

    if (item) {
      if (quantity <= 0) {
        this.removeFromCart(productId);
      } else {
        item.quantity = quantity;
        this.updateCart(currentItems);
      }
    }
  }

  /**
   * Vide le panier
   */
  clearCart(): void {
    this.updateCart([]);
  }

  /**
   * Met à jour le panier et le sauvegarde
   */
  private updateCart(items: CartItem[]): void {
    this.cartItems.set(items);
    localStorage.setItem('cart', JSON.stringify(items));
  }
}