export interface Product {
  id: number;
  name: string;
  description: string;
  price: number;
  sku: string;
  images: string[];
  categoryId?: number;
  categoryName?: string;
  stockQuantity: number;
  active: boolean;
  averageRating: number;
  weight: number;
  createdAt: Date;
}

export interface ProductPage {
  content: Product[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

export interface Category {
  id: number;
  name: string;
  description: string;
}