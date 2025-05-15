export interface CrepePlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
  startAccessibilityService(): Promise<void>;
  stopAccessibilityService(): Promise<void>;
  getAccessibilityData(): Promise<{ data: string }>;
  initializeGraphQuery(): Promise<{ success: boolean }>;
  updateSnapshot(): Promise<{ success: boolean }>;
  queryGraph(options: { pattern: string }): Promise<{ results: any[] }>;
  matchCollectorData(options: { collectorData: string }): Promise<{ matches: any[] }>;
}