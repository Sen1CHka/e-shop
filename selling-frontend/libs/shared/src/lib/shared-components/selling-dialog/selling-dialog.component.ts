import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  EventEmitter,
  HostListener,
  Input,
  Output,
  ViewChild,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { SellingIconComponent } from '../selling-icon';

@Component({
  selector: 'selling-dialog',
  standalone: true,
  imports: [CommonModule, SellingIconComponent],
  templateUrl: './selling-dialog.component.html',
  styleUrl: './selling-dialog.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SellingDialogComponent {
  @Input()
  dialogTitle = '';

  @Input()
  wide = false;

  @Input()
  isVisible = false;

  @Output()
  closeDialogEvent = new EventEmitter<boolean>();

  // Keyboard focus moves into the dialog as soon as it is drawn
  @ViewChild('panel')
  set panel(panel: ElementRef<HTMLElement> | undefined) {
    panel?.nativeElement.focus();
  }

  @HostListener('document:keydown.escape')
  onEscape() {
    if (this.isVisible) {
      this.closeDialog();
    }
  }

  onBackdrop(event: MouseEvent) {
    if (event.target === event.currentTarget) {
      this.closeDialog();
    }
  }

  closeDialog() {
    this.closeDialogEvent.emit(false);
  }
}
