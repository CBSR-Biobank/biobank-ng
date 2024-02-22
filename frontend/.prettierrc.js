const config = {
  trailingComma: 'es5',
  tabWidth: 2,
  useTabs: false,
  semi: true,
  singleQuote: true,
  printWidth: 120,
  tailwindConfig: './tailwind.config.js',
  tailwindFunctions: ['cn', 'clsx'],
  plugins: ['@trivago/prettier-plugin-sort-imports', 'prettier-plugin-organize-imports', 'prettier-plugin-tailwindcss'],
  importOrder: ['^@app/(.*)$', '<THIRD_PARTY_MODULES>', '^[./]'],
  importOrderSeparation: true,
  importOrderSortSpecifiers: true,
  importOrderCaseInsensitive: false,
};

export default config;
