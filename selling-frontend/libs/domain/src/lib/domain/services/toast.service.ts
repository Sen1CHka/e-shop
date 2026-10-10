import { Injectable, signal } from '@angular/core';

export type ToastType = 'success' | 'error' | 'info';

export interface ToastAction {
  label: string;
  run: () => void;
}

export interface Toast {
  id: number;
  type: ToastType;
  text: string;
  action?: ToastAction;
}

@Injectable({ providedIn: 'root' })
export class ToastService {
  private nextId = 1;

  readonly toasts = signal<Toast[]>([]);

  success(text: string, action?: ToastAction) {
    this.show('success', text, action);
  }

  error(text: string) {
    this.show('error', text);
  }

  info(text: string, action?: ToastAction) {
    this.show('info', text, action);
  }

  dismiss(id: number) {
    this.toasts.update((toasts) => toasts.filter((toast) => toast.id !== id));
  }

  private show(type: ToastType, text: string, action?: ToastAction) {
    const id = this.nextId++;
    this.toasts.update((toasts) => [...toasts.slice(-3), { id, type, text, action }]);
    // Errors and messages with an action stay longer so there is time to read and react
    setTimeout(() => this.dismiss(id), type === 'error' || action ? 7000 : 4000);
  }
}
