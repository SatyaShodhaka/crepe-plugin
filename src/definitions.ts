export interface CrepePlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
  startAccessibilityService(): Promise<void>;
  stopAccessibilityService(): Promise<void>;
  getAccessibilityData(): Promise<{ data: string }>;
}