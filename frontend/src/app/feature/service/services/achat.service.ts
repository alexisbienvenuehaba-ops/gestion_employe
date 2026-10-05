
import { Injectable, inject, signal } from '@angular/core'
import { HttpClient } from '@angular/common/http'
import { achat } from '../../model/model/achat.model'

@Injectable({
  providedIn: 'root'
})
export class AchatService {

  private readonly http = inject(HttpClient)

  private readonly apiUrl = 'http://localhost:8080/api/achat'

  readonly achats = signal<achat[]>([])

  getAll(): void {
    this.http.get<achat[]>(this.apiUrl)
      .subscribe({
        next: (data) => {
          this.achats.set(data)
        }
      })
  }

  add(data: any): void {
    this.http.post<achat>(this.apiUrl, data)
      .subscribe({
        next: (newAchat) => {
          this.achats.update(achats => [
            ...achats,
            newAchat
          ])
        }
      })
  }

  update(id: number, data: any): void {
    this.http.put<achat>(
      `${this.apiUrl}/${id}`,
      data
    ).subscribe({
      next: (updatedAchat) => {
        this.achats.update(achats =>
          achats.map(a =>
            a.idAchat === id
              ? updatedAchat
              : a
          )
        )
      }
    })
  }

  delete(id: number): void {
    this.http.delete(
      `${this.apiUrl}/${id}`
    ).subscribe({
      next: () => {
        this.achats.update(achats =>
          achats.filter(a =>
            a.idAchat !== id
          )
        )
      }
    })
  }
}

