import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Custodia e2e test', () => {

    let navBarPage: NavBarPage;
    let custodiaDialogPage: CustodiaDialogPage;
    let custodiaComponentsPage: CustodiaComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Custodias', () => {
        navBarPage.goToEntity('custodia');
        custodiaComponentsPage = new CustodiaComponentsPage();
        expect(custodiaComponentsPage.getTitle()).toMatch(/bcbApp.custodia.home.title/);

    });

    it('should load create Custodia dialog', () => {
        custodiaComponentsPage.clickOnCreateButton();
        custodiaDialogPage = new CustodiaDialogPage();
        expect(custodiaDialogPage.getModalTitle()).toMatch(/bcbApp.custodia.home.createOrEditLabel/);
        custodiaDialogPage.close();
    });

    it('should create and save Custodias', () => {
        custodiaComponentsPage.clickOnCreateButton();
        custodiaDialogPage.statusSelectLastOption();
        custodiaDialogPage.setDataAberturaInput(12310020012301);
        expect(custodiaDialogPage.getDataAberturaInput()).toMatch('2001-12-31T02:30');
        custodiaDialogPage.setDataFechamentoInput(12310020012301);
        expect(custodiaDialogPage.getDataFechamentoInput()).toMatch('2001-12-31T02:30');
        custodiaDialogPage.setQuantidadeTotalInput('5');
        expect(custodiaDialogPage.getQuantidadeTotalInput()).toMatch('5');
        custodiaDialogPage.setValorTotalInput('5');
        expect(custodiaDialogPage.getValorTotalInput()).toMatch('5');
        custodiaDialogPage.save();
        expect(custodiaDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class CustodiaComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-custodia div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class CustodiaDialogPage {
    modalTitle = element(by.css('h4#myCustodiaLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    statusSelect = element(by.css('select#field_status'));
    dataAberturaInput = element(by.css('input#field_dataAbertura'));
    dataFechamentoInput = element(by.css('input#field_dataFechamento'));
    quantidadeTotalInput = element(by.css('input#field_quantidadeTotal'));
    valorTotalInput = element(by.css('input#field_valorTotal'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setStatusSelect = function (status) {
        this.statusSelect.sendKeys(status);
    }

    getStatusSelect = function () {
        return this.statusSelect.element(by.css('option:checked')).getText();
    }

    statusSelectLastOption = function () {
        this.statusSelect.all(by.tagName('option')).last().click();
    }
    setDataAberturaInput = function (dataAbertura) {
        this.dataAberturaInput.sendKeys(dataAbertura);
    }

    getDataAberturaInput = function () {
        return this.dataAberturaInput.getAttribute('value');
    }

    setDataFechamentoInput = function (dataFechamento) {
        this.dataFechamentoInput.sendKeys(dataFechamento);
    }

    getDataFechamentoInput = function () {
        return this.dataFechamentoInput.getAttribute('value');
    }

    setQuantidadeTotalInput = function (quantidadeTotal) {
        this.quantidadeTotalInput.sendKeys(quantidadeTotal);
    }

    getQuantidadeTotalInput = function () {
        return this.quantidadeTotalInput.getAttribute('value');
    }

    setValorTotalInput = function (valorTotal) {
        this.valorTotalInput.sendKeys(valorTotal);
    }

    getValorTotalInput = function () {
        return this.valorTotalInput.getAttribute('value');
    }

    save() {
        this.saveButton.click();
    }

    close() {
        this.closeButton.click();
    }

    getSaveButton() {
        return this.saveButton;
    }
}
