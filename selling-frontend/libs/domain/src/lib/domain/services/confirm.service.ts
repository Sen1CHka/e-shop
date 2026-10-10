import { Injectable, signal } from '@angular/core';

export interface ConfirmRequest {
  title: string;
  text: string;
  confirmLabel: string;
  cancelLabel?: string;
  danger?: boolean;
}

/**
 * Asks the user to confirm an action that cannot be undone.
 * The question is drawn by the confirm dialog placed once in the app root.
 */
@Injectable({ providedIn: 'root' })
export class ConfirmService {
  private resolve?: (confirmed: boolean) => void;

  readonly request = signal<ConfirmRequest | null>(null);

  ask(request: ConfirmRequest): Promise<boolean> {
    this.resolve?.(false);
    this.request.set(request);
    return new Promise((resolve) => (this.resolve = resolve));
  }

  answer(confirmed: boolean) {
    this.request.set(null);
    this.resolve?.(confirmed);
    this.resolve = undefined;
  }
}
