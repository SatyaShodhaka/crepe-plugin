export interface CrepePlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
