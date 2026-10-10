import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AbstractControl, FormGroup } from '@angular/forms';

/**
 * Label, projected input and validation message of one form field.
 * The message appears only after the user has left the field or tried to submit.
 */
@Component({
  selector: 'selling-field',
  standalone: true,
  imports: [CommonModule],
  template: `
    <label class="form-label" [attr.for]="for">
      {{ label }}
      <span *ngIf="optional" class="text-secondary fw-normal">(optional)</span>
    </label>
    <ng-content></ng-content>
    <div class="field-message">
      <div *ngIf="message as text; else hintText" class="field-error" role="alert">{{ text }}</div>
      <ng-template #hintText>
        <div *ngIf="hint" class="form-text">{{ hint }}</div>
      </ng-template>
    </div>
  `,
  // Room for the message is always kept, so a message appearing on blur does not move
  // the rest of the form away from under the pointer
  styles: [':host { display: block; } .field-message { min-height: 1.6rem; }'],
})
export class SellingFieldComponent {
  @Input({ required: true })
  label!: string;

  @Input({ required: true })
  control!: AbstractControl | null;

  /** Id of the projected input, connects the label with it */
  @Input()
  for?: string;

  @Input()
  hint?: string;

  @Input()
  optional = false;

  get message(): string | null {
    const control = this.control;
    if (!control || !control.errors || !(control.touched || control.dirty)) {
      return null;
    }
    const errors = control.errors;
    if (errors['server']) return errors['server'];
    if (errors['required']) return `${this.label} is required`;
    if (errors['email']) return 'Enter a valid e-mail address, e.g. name@example.com';
    if (errors['minlength']) return `${this.label} must have at least ${errors['minlength'].requiredLength} characters`;
    if (errors['maxlength']) return `${this.label} can have at most ${errors['maxlength'].requiredLength} characters`;
    if (errors['min']) return `${this.label} must be at least ${errors['min'].min}`;
    if (errors['max']) return `${this.label} can be at most ${errors['max'].max}`;
    if (errors['pattern']) return `${this.label} has an invalid format`;
    if (errors['mismatch']) return 'The passwords do not match';
    return `${this.label} is not valid`;
  }
}

/** Shows messages sent by the server next to the fields they belong to */
export function applyServerErrors(form: FormGroup, errors: Record<string, string>) {
  for (const [field, message] of Object.entries(errors)) {
    const control = form.get(field);
    if (control) {
      control.setErrors({ server: message });
      control.markAsTouched();
    }
  }
}
