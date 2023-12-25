import { ChangeDetectionStrategy, Component, EventEmitter, Input, Output, TemplateRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SellingButtonComponent } from '../selling-button';

@Component({
  selector: 'selling-dialog',
  standalone: true,
  imports: [CommonModule, SellingButtonComponent],
  templateUrl: './selling-dialog.component.html',
  styleUrl: './selling-dialog.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SellingDialogComponent {

  @Input()
  contentTemplate?: TemplateRef<any>;

  @Input()
  isVisible: boolean = false;

  @Output()
  closeDialogEvent = new EventEmitter<boolean>();

  closeDialog(){
    this.closeDialogEvent.emit(false);
  }
}
