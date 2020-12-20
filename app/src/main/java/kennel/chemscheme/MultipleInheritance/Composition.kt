package kennel.chemscheme.MultipleInheritance

class Composition (
        firstCringe: FirstCringe = FCringe(),
        secondCringe: SecondCringe = SCringe()) : FirstCringe by firstCringe, SecondCringe by secondCringe{
}